// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/sqlrest
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2024/3/31
// Location: beijing , china
/////////////////////////////////////////////////////////////
package io.modelcontextprotocol.server.transport;

public interface WebMvcSseServerAuthChecker {

  String getTokenParamName();

  boolean checkTokenValid(String token);
}
