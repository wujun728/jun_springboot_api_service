package com.yatop.lambda.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 静态资源
 *
 * @author lrz
 */
@Controller
public class StaticsController {
	@RequestMapping(value = "/statics/**", method = RequestMethod.GET)
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getParameter("path");
		String filePath = StringUtils.substringAfterLast(path, "/");

		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(path);
		Optional<MediaType> o = MediaTypeFactory.getMediaType(filePath);
		if (o.isPresent()) {
			response.setContentType(o.get().toString());
		}
		response.setCharacterEncoding("UTF-8");
		if (inputStream != null) {
			try (OutputStream output = response.getOutputStream()) {
				stream(inputStream, output);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("响应失败", e);
			}
		} else {
			response.setStatus(404);
		}
	}

	public static long stream(InputStream input, OutputStream output) throws IOException {
		try (
				ReadableByteChannel inputChannel = Channels.newChannel(input);
				WritableByteChannel outputChannel = Channels.newChannel(output);
		) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
			long size = 0;

			while (inputChannel.read(buffer) != -1) {
				buffer.flip();
				size += outputChannel.write(buffer);
				buffer.clear();
			}

			return size;
		}
	}
}
