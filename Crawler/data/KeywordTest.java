160
https://raw.githubusercontent.com/c0ny1/java-object-searcher/master/src/test/java/me/gv7/tools/josearcher/entity/KeywordTest.java
package me.gv7.tools.josearcher.entity;


class KeywordTest {
    public static void main(String[] args) {
        Keyword key = new Keyword.Builder().setField_name("entity").setField_value("111").setField_type("wewe").build();
        System.out.println(key);
    }
}