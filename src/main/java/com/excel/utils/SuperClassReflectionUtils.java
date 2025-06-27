package com.excel.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.record.StyleRecord;

public class SuperClassReflectionUtils {

	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, true)) {
			fields.addAll(Arrays.asList(clazzInClasses.getDeclaredFields()));
		}
		return fields;
	}

	/**
	 * clazz에 멤버 변수 중 name 과 동일한 필드명에 할당된 데이터를 반환
	 * @param clazz :엑셀로 생성할 데이터가 참조하고 있는 객체
	 * @param name : 엑셀에 작성하고자 하는 필드명
	 * @return
	 * @throws Exception
	 */
	//Class로 전달된 타입의 객체의 특정 필드 조회 (엑셀로 생성할 데이터가 참조하고 있는 객체(clazz)
	public static Field getField(Class<?> clazz, String name) throws Exception {
		for(Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, false)) { //clazz의 super Class 모두 가져오기(자식, 자식의 부모, 자식의 부모의 부모.... 순으로 반환)
			for (Field field : clazzInClasses.getDeclaredFields()) {
				if (field.getName().equals(name)) { //생성하려는 엑셀 필드명과 동일하면 필드값 리턴
					return clazzInClasses.getDeclaredField(name);
				}
			}
		}
		throw new NoSuchFieldException();
	}

	/**
	 * 엑셀 파일로 작성된 데이터가 담긴 클래스를 포함한 연관된 부모 클래스를 리스트로 출력해준다.
	 * @param clazz : 엑셀 파일로 작성하려는 데이터가 담긴 클래스
	 * @param fromSuper : false: 최상위 부모 클래스부터 맨 마지막 자손 순으로 정렬. true: 자손부터 최상위 부모 순으로 정렬.
	 * @return
	 */
	private static List<Class<?>>  getAllClassesIncludingSuperClasses(Class<?> clazz, boolean fromSuper) {
		List<Class<?>> classes = new ArrayList<>();
		while (clazz != null) {
			classes.add(clazz);
			clazz = clazz.getSuperclass();
		}
		return classes;
	}
}
