package com.excel.controller;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.excel.entity.Member;
import com.excel.repository.MemberRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ViewController {
	private final MemberRepository memberRepository;
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/v2")
	public ModelAndView goToVer2(ModelAndView mv) {
		List<Member> results = memberRepository.findAll();
		mv.addObject("members", results);
		mv.setViewName("v2");
		return mv;
	}
}
