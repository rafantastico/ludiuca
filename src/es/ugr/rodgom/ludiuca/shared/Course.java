/*******************************************************************************
 * Ludiuca
 *
 * Copyright (C) (c) 2015 Ludiuca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package es.ugr.rodgom.ludiuca.shared;

import java.io.Serializable;

public class Course implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private int year;
	private String name;
	
	public Course(){
	}
	
	public Course(int id, int year, String name){
		this.id = id;
		this.year = year;
		this.name = name;		
	}
	
	public int getId(){
		return id;
	}
	
	public int getYear(){
		return year;
	}
	
	public String getName(){
		return name;
	}
}
