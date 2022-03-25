package com.binar.grab.junit;

public class Test {
    public static void main(String[] args){
        String s = "/abc/def/ghfj.doc";
        s.substring(s.lastIndexOf(".") );
        System.out.println( s.substring(s.lastIndexOf(".")+1));
    }
}
