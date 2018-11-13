package com.yeepay.g3.app.nccashier.wap.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

@Controller
@RequestMapping("/")
public class IndexAction {
	static final Logger logger = LoggerFactory.getLogger(IndexAction.class);

	@RequestMapping(value = "{name}", produces = { "text/plain;charset=UTF-8" })
	@ResponseBody
	public String mpFileRequest(@PathVariable String name) {
		if (StringUtils.isNotBlank(name)) {
			if (!name.endsWith(".txt")) {
				name = name + ".txt";
			}
			File file = new File("/apps/shouyin/" + name);
			if (file.exists()) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					StringBuilder sb = new StringBuilder();
					String line;
					while (null != (line = br.readLine())) {
						sb.append(line);
					}
					return sb.toString();
				} catch (Exception e) {
					logger.error("读取微信公众号授权文件失败", e);
				}

			}
		}
		return "param error";
	}
}
