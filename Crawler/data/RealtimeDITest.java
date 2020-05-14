8
https://raw.githubusercontent.com/nataraz123/Spring/master/IOCProj43-RealtimeDI-StrategyDP-AC-Propertiesfile/src/com/nt/test/RealtimeDITest.java
package com.nt.test;

import java.util.Scanner;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.nt.controller.MainController;
import com.nt.vo.StudentVO;

public class RealtimeDITest {

	public static void main(String[] args) {
		BeanFactory factory=null;
		MainController controller=null;
		Scanner sc=null;
		String sname=null,sadd=null,m1=null,m2=null,m3=null;
		StudentVO vo=null;
		String result=null;
				//create IOC container
		factory=new XmlBeanFactory(new ClassPathResource("com/nt/cfgs/applicationContext.xml"));
		//get Controller class object
		controller=factory.getBean("controller",MainController.class);
		//read inputs 
		sc=new Scanner(System.in);
		System.out.println("Enter student name::");
		sname=sc.next();
		System.out.println("Enter student address::");
		sadd=sc.next();
		System.out.println("Enter student Marks1::");
		m1=sc.next();
		System.out.println("Enter student Marks2::");
		m2=sc.next();
		System.out.println("Enter student Marks3::");
		m3=sc.next();
		//prepare VO class object
		vo=new StudentVO();
		 vo.setSname(sname); vo.setSadd(sadd);
		vo.setM1(m1); vo.setM2(m2); vo.setM3(m3);
		
		//invoke method
		try {
			result=controller.handleStudent(vo);
			System.out.println(result);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("STudent Registration failed");
		}
		
		
		
		

	}//main
}//class