package com.thinking.machines.common;
import java.lang.reflect.*;
import java.util.*;

public class POJOCopier
{
private POJOCopier(){}
public static void copy(Object target,Object source)
{
List<Method> setters=new ArrayList<>();
List<Method> getters=new ArrayList<>();
Class targetClass=target.getClass();
Method targetMethods[];
targetMethods=targetClass.getMethods();
int e;
Method method;
String methodName;
Class parameterTypes[];
Class returnType;
//code to get setters
for(e=0;e<targetMethods.length;e++)
{
method=targetMethods[e];
methodName=method.getName();
if(methodName.length()<=3) continue;
if(methodName.startsWith("set")==false) continue;
parameterTypes=method.getParameterTypes();
if(parameterTypes.length!=1) continue;
returnType=method.getReturnType();
if(returnType.getName().equals("void")==false) continue;
char m=methodName.charAt(3);
if(m<65 || m>90) continue;
// System.out.println("Adding "+methodName+" to setters list");
setters.add(method);
}
if(setters.size()==0) return;

// code to get getters
Class sourceClass=source.getClass();
Method setterMethod;
Method getterMethod;
String setterName;
String getterName;
int i=0;
String propertyName;
while(i<setters.size())
{
setterMethod=setters.get(i);
parameterTypes=setterMethod.getParameterTypes();
setterName=setterMethod.getName();
// System.out.println("Name of setter method: "+setterName);
propertyName=setterName.substring(3);
getterName="get"+propertyName;
// System.out.println("Looking for getter:"+getterName);
try
{
getterMethod=sourceClass.getMethod(getterName,new Class[0]);
returnType=getterMethod.getReturnType();
if(returnType.equals(parameterTypes[0])==false)
{
setters.remove(i);
continue;
}
// System.out.println("Getter Method found: "+getterMethod.getName());
getters.add(getterMethod);
}catch(NoSuchMethodException nsme)
{
setters.remove(i);
continue;
}
i++;
}
// System.out.println("Data Structure of setter/getter created");
Object getterResult;
i=0;
while(i<setters.size())
{
setterMethod=setters.get(i);
getterMethod=getters.get(i);
// System.out.println(setterMethod.getName()+"---->"+getterMethod.getName());
try
{
getterResult=getterMethod.invoke(source,new Object[0]);
Class getterResultClass=getterResult.getClass();
if(isPrimitive(getterResultClass))
{
setterMethod.invoke(target,getterResult);
}
else
{
Object getterResultClassObject=getterResultClass.newInstance();
copy(getterResultClassObject,getterResult);
setterMethod.invoke(target,getterResult);
}
}catch(Exception ee)
{
// do nothing
}
i++;
}
}

public static boolean isPrimitive(Class c)
{
boolean result=false;
String dataType=c.getName();
if(dataType.equals("int") || dataType.equals("java.lang.Integer")) result=true;
if(dataType.equals("long") || dataType.equals("java.lang.Long")) result=true;
if(dataType.equals("float") || dataType.equals("java.lang.Float")) result=true;
if(dataType.equals("char") || dataType.equals("java.lang.Character")) result=true;
if(dataType.equals("short") || dataType.equals("java.lang.Short")) result=true;
if(dataType.equals("byte") || dataType.equals("java.lang.Byte")) result=true;
if(dataType.equals("double") || dataType.equals("java.lang.Double")) result=true;
if(dataType.equals("boolean") || dataType.equals("java.lang.Boolean")) result=true;
if(dataType.equals("java.lang.String")) result= true;
return result;
} 

}
