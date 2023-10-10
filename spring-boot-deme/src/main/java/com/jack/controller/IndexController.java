package com.jack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Jacky<workinglang@163.com>
 * Created At: 2023/10/10 12:07
 * <p></p>
 */
@RestController
public class IndexController {
	@GetMapping("/")
	public Map<String, Object> index() {
		HashMap<String, Object> data = new HashMap<>();
		data.put("time", new Date());
		data.put("data", Collections.EMPTY_MAP);
		data.put("code", HttpStatus.OK.value());
		data.put("msg", "success");
		return data;
	}
}
