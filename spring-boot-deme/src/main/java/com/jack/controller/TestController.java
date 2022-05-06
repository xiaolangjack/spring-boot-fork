package com.jack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By: yy<lanqiu@deloitte.com.cn>
 * Created At: 2022/4/28 0:24
 * <p></p>
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/{name}")
	public Map<String, Object> index(@PathVariable(required = false) String name) {
		HashMap<String, Object> data = new HashMap<>();
		data.put("data", name);
		data.put("code", HttpStatus.OK.value());
		data.put("msg", "success");
		return data;
	}
}
