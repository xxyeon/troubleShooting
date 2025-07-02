# 팩토리 패턴과 리플렉션으로 동적으로 객체 생성
파라미터로 엑셀 파일에 들어갈 메타 정보가 포함된 객체를 전달받습니다. Class<?>를 통해 해당 객체의 메타 정보를 조회하여 엑셀의 기본 구성을 하는 헤더와 바디에 필요한 필드명을 가져옵니다.
```java
/**
 *
 * @param type Class type to be rendered
 * @return ExcelRenderResource: 동일한 타입이면 재사용
 */
public static ExcelRenderResource prepareRenderResource(Class<? extends BaseXlsx> type) {

  if(cachedResources.containsKey(type)) {
    return cachedResources.get(type);
  }
  Map<String, String> headerNamesMap = new LinkedHashMap<>();
  List<String> fieldNames = new ArrayList<>();

  for(Field field : getAllFields(type)) {
    if (field.isAnnotationPresent(ExcelColumn.class)) {
      ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
      fieldNames.add(field.getName());
      headerNamesMap.put(field.getName(), annotation.headerName());
    }
  }

  ExcelRenderResource resource = new ExcelRenderResource(headerNamesMap, fieldNames);
  cachedResources.put(type, resource);
  return resource;
}
```

- @param type: 엑셀 파일로 작성되어져야 하는 메타 정보를 가진 클래스 타입입니다.    
- @return ExcelRenderResource: 해당 객체에는 type 객체의 메타 정보를 바탕으로 헤더와 바디로 나눠서 관리하고 있습니다. 헤더는 Map 형태로, 엑셀 바디로 작성되는 필드명은 List로 관리하고 있습니다.
# 싱글톤 패턴 적용하여 객체 생성 낭비 해결
웹 서버에 엑셀 메타 정보를 포함한 인스턴스를 캐싱해두고, 만일 동일한 타입의 객체를 생성한다면 캐시에서 조회하여 재사용합니다.
```java
 if(cachedResources.containsKey(type)) {
    return cachedResources.get(type);
  }
```
위 조건을 추가하여 웹 서버에 캐싱된 인스턴스가 있다면 인스턴스를 재사용하도록 구현했습니다.
# 커스텀 어노테이션 구현하여 객체마다 엑셀 헤더 값 설정
## 어노테이션 적용 전
기존에는 헤더 값을 설정하기 위해 하드코딩으로 구현했습니다.
```java
String[] headers = {"스캔 날짜", "고객사", "Account Name", "Account ID", "서비스", "기능", "Resource ID", "tag", "Json"};
```
## 어노테이션 적용 후
`@ExcelColumn` 커스텀 어노테이션을 구현하여 아래 코드 처럼 엑셀 파일에 출력될 헤더명을 명시할 수 있습니다.
```java
@Target(ElementType.FIELD) //필드에 붙일 어노테이션이다.
@Retention(RetentionPolicy.RUNTIME) //런타임에 동작한다.
public @interface ExcelColumn {
	String headerName() default "";
}
```

```java
@ExcelColumn(headerName = "스캔 날짜")
private String scanTime;
```
파일로 출력되는 결과는 아래 사진과 같습니다.
![image](https://github.com/user-attachments/assets/f551040e-5b3a-4cb6-bdd0-93a9d9dbcf88)
