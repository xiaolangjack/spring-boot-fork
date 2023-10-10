package com.jack.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By: Jacky<workinglang@163.com>
 * Created At: 2023/10/10 12:07
 * <p></p>
 */
@RestController
public class IndexController {
	@GetMapping("/")
	public String index() {
		return "<!DOCTYPE html>\n" +
				"<html lang=\"en\">\n" +
				"<head>\n" +
				"    <meta charset=\"UTF-8\">\n" +
				"    <title>SpringBoot 源码阅读</title>\n" +
				"    <style>\n" +
				"        body{\n" +
				"            background: #d4cddc;\n" +
				"            color: #2b686a\n" +
				"        }\n" +
				"    </style>\n" +
				"</head>\n" +
				"<body>\n" +
				"<div style=\"text-align: center;\">\n" +
				"    <h1 style=\"width: 100%;\"> SpringBoot 源码阅读</h1>\n" +
				"    <br><h4 style=\"width: 100%;\">Jacky Qiu&lt;workinglang@163.com&gt;</h4>\n" +
				"</div>\n" +
				"</body>\n" +
				"</html>";
	}
}
