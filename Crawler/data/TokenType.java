2
https://raw.githubusercontent.com/zcy1010/compilerExperimentSyntaxAnalysis/master/src/com/company/wordAnalysis/TokenType.java
package com.company.wordAnalysis;

public enum TokenType {
    //类型
    IDENTIFIER,         //标识符
    INTEGERLITERAL,     //整数

    //保留字
    CLASS,
    PUBLIC,
    STATIC,
    VOID,
    MAIN,
    STRING,
    EXTENDS,
    RETURN,
    INT,
    BOOLEAN,
    IF,
    ELSE,
    WHILE,
    SYSTEMOUTPRINTLN,
    LENGTH,
    TRUE,
    FALSE,
    THIS,
    NEW,


    //专有符号
//    [ ]    ( )  { }	  ,	 ;   =     &&
//    <  +  -  *  .    !
    /**
     * [
     */
    LBRACKET,//[
    /**
     * ]
     */
    RBRACKET,//]
    /**
     * (
     */
    LPAREN,//(
    /**
     * )
     */
    RPAREN,//)
    /**
     * {
     */
    LBRACE,//{
    /**
     * }
     */
    RBRACE,//}
    /**
     * ,
     */
    COMMA,//,
    /**
     * ;
     */
    SEMICOLON,//;
    /**
     * =
     */
    EQUAL,//=
    /**
     * &&
     */
    DOUBLEAND,//&&
    /**
     * &
     */
    AND,
    /**
     * <
     */
    LESSTHEN,//<
    /**
     * +
     */
    PLUS,//+
    /**
     * -
     */
    HYPHEN,//-
    /**
     * *
     */
    MULTIPLY,//*
    /**
     * .
     */
    FULLSTOP,
    /**
     * !
     */
    EXCLAMATION,//!


    //空白符
    SPACE,
    TABLE,
    ENTER,

    ERROR,
    NULL,

}
