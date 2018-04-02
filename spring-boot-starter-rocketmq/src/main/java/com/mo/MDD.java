/** 
* Project Name:spring-boot-starter-rocketmq 
* Package Name:com.mo 
* File Name:MDD.java 
* Date:2018年4月2日下午2:23:34 
* 
* Copyright (c) 2016-2018, Maike Tech
*
* Licensed under the Maike License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.maike51.com/licenses/LICENSE-1.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.mo;

/**
 * ClassName: MDD <br/>
 * Function: TODO 功能说明. <br/>
 * 
 * date: 2018年4月2日 下午2:23:34 <br/>
 * 
 * @author shuangyu
 * @version
 * @since JDK 1.8
 */
public class MDD {

	public static void main(String[] args) {
		System.out.println("333");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {

				System.out.println("22222");

			}

		}));

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {

				System.out.println("1111");

			}

		}));
		System.out.println("3333333333333333");

	}
}
