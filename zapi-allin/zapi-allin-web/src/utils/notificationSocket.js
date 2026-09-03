import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
/**
 * 全局通知 WebSocket 管理器
 * 负责建立 STOMP 连接、订阅消息并将事件发布给监听者
 */
class NotificationSocket {
  constructor() {
    this.client = null
    this.connectedUserId = null
    this.subscriptions = []
    this.listeners = new Set()
  }

  /**
   * 构建 WebSocket 端点地址
   */
  get socketUrl() {
    const apiBase = (import.meta.env.VITE_API_BASE ?? '/zapi-allin-sys').replace(/\/$/, '')
    const endpoint = (import.meta.env.VITE_WS_ENDPOINT ?? '/ws/notification')
    if (/^https?:\/\//i.test(endpoint)) {
      return endpoint
    }
    const normalizedEndpoint = endpoint.startsWith('/') ? endpoint : `/${endpoint}`
    if (!apiBase) {
      return `${window.location.origin}${normalizedEndpoint}`
    }
    return `${window.location.origin}${apiBase}${normalizedEndpoint}`
  }

  /**
   * 建立或复用连接
   * @param {number|string} userId 当前登录用户ID
   */
  connect(userId) {
    if (!userId) return

    // 已连接且用户相同，直接复用
    if (this.client && this.client.connected && this.connectedUserId === userId) {
      return
    }

    this.disconnect()
    this.connectedUserId = userId

    this.client = new Client({
      reconnectDelay: 5000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      debug: () => {},
      webSocketFactory: () => new SockJS(this.socketUrl)
    })

    this.client.onConnect = () => {
      this.subscribeChannels()
    }

    this.client.onStompError = (frame) => {
      console.error('WebSocket STOMP error:', frame?.headers?.message || frame)
    }

    this.client.onWebSocketClose = () => {
      this.cleanupSubscriptions()
    }

    this.client.activate()
  }

  /**
   * 订阅服务端推送的目的地
   */
  subscribeChannels() {
    if (!this.client || !this.client.connected) return

    this.cleanupSubscriptions()

    const userTopic = this.connectedUserId ? `/topic/notification/${this.connectedUserId}` : null

    if (userTopic) {
      this.subscriptions.push(
        this.client.subscribe(userTopic, (message) => {
          this.emit('personal', message)
        })
      )
    }

    this.subscriptions.push(
      this.client.subscribe('/topic/system', (message) => {
        this.emit('system', message)
      })
    )
  }

  /**
   * 发布消息给所有监听者
   */
  emit(channel, rawMessage) {
    if (!rawMessage) return
    let payload = rawMessage.body
    try {
      payload = payload ? JSON.parse(payload) : null
    } catch (error) {
      payload = rawMessage.body
    }

    const event = { channel, payload }
    this.listeners.forEach((listener) => {
      try {
        listener(event)
      } catch (err) {
        console.error('Notification socket listener error:', err)
      }
    })
  }

  /**
   * 添加监听者
   * @param {Function} handler
   * @returns {Function} 取消订阅函数
   */
  subscribe(handler) {
    if (typeof handler !== 'function') {
      return () => {}
    }
    this.listeners.add(handler)
    return () => {
      this.listeners.delete(handler)
    }
  }

  /**
   * 清理 STOMP 订阅
   */
  cleanupSubscriptions() {
    this.subscriptions.forEach((sub) => {
      try {
        sub?.unsubscribe?.()
      } catch (error) {
        console.error('Unsubscribe error:', error)
      }
    })
    this.subscriptions = []
  }

  /**
   * 主动断开连接
   */
  disconnect() {
    this.cleanupSubscriptions()
    if (this.client) {
      try {
        this.client.deactivate()
      } catch (error) {
        console.error('Deactivate socket error:', error)
      }
    }
    this.client = null
    this.connectedUserId = null
  }
}

const notificationSocket = new NotificationSocket()
export default notificationSocket

