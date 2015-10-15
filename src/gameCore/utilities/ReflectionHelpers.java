package gameCore.utilities;

import java.lang.reflect.Modifier;

public class ReflectionHelpers
{
	public static boolean isValueType(Class<?> targetType)
	{
		if (targetType == null)
		{
			throw new NullPointerException("Must supply the targetType parameter");
		}
		// NOTE: Added this since struct are considere value types (primitives) in C#
		//       and since we are using wrapper Classes for our primitives
//		System.out.println(targetType.getSimpleName());
		switch (targetType.getSimpleName())
		{
			// TODO: Need to add other types here
			case "Integer":
			case "Character":
			case "Rectangle":
			case "Vector2":
			case "Vector3":
			case "Vector4":
				return true;
		}
		// NOTE: This only works on "real" primitives not their wrapper types
		return (targetType.isEnum() || targetType.isPrimitive());
	}

	public static Class<?> getBaseType(Class<?> targetType)
	{
		if (targetType == null)
		{
			throw new NullPointerException("Must supply the targetType parameter");
		}
// #if WINRT
		// var type = targetType.GetTypeInfo().BaseType;
// #else
		Class<?> type = targetType.getSuperclass();
// #endif
		return type;
	}

	// / <summary>
	// / Returns true if the given type represents a class that is not abstract
	// / </summary>
	public static boolean isConcreteClass(Class<?> t)
	{
		if (t == null)
		{
			throw new NullPointerException("Must supply the t (type) parameter");
		}
// #if WINRT
		// var ti = t.GetTypeInfo();
		// if (ti.IsClass && !ti.IsAbstract)
		// return true;
// #else
		// if (t.IsClass && !t.IsAbstract)
		if (Modifier.isAbstract(t.getModifiers()))
			return true;
// #endif
		return false;
	}

	// public static MethodInfo GetPropertyGetMethod(PropertyInfo property)
	// {
	// if (property == null)
	// {
	// throw new NullPointerException("Must supply the property parameter");
	// }
	//
// #if WINRT
	// return property.GetMethod;
// #else
	// return property.GetGetMethod();
// #endif
	// }

	// public static MethodInfo GetPropertySetMethod(PropertyInfo property)
	// {
	// if (property == null)
	// {
	// throw new NullPointerException("Must supply the property parameter");
	// }
	//
// #if WINRT
	// return property.SetMethod;
// #else
	// return property.GetSetMethod();
// #endif
	// }

	// public static Attribute GetCustomAttribute(MemberInfo member, Type memberType)
	// {
	// if (member == null)
	// {
	// throw new NullPointerException("Must supply the member parameter");
	// }
	// if (memberType == null)
	// {
	// throw new NullPointerException("Must supply the memberType parameter");
	// }
// #if WINRT
	// return member.GetCustomAttribute(memberType);
// #else
	// return Attribute.GetCustomAttribute(member, memberType);
// #endif
	// }

	// / <summary>
	// / Returns true if the get and set methods of the given property exist and are public
	// / </summary>
	// public static bool PropertyIsPublic(PropertyInfo property)
	// {
	// if (property == null)
	// {
	// throw new NullPointerException("Must supply the property parameter");
	// }
	//
	// var getMethod = GetPropertyGetMethod(property);
	// if (getMethod == null || !getMethod.IsPublic)
	// return false;
	//
	// var setMethod = GetPropertySetMethod(property);
	// if (setMethod == null || !setMethod.IsPublic)
	// return false;
	//
	// return true;
	// }

	// / <summary>
	// / Returns true if the given type can be assigned the given value
	// / </summary>
	public static boolean isAssignableFrom(Class<?> type, Object value)
	{
		if (type == null)
			throw new NullPointerException("type");
		if (value == null)
			throw new NullPointerException("value");

		return isAssignableFromType(type, value.getClass());
	}

	// / <summary>
	// / Returns true if the given type can be assigned a value with the given object type
	// / </summary>
	public static boolean isAssignableFromType(Class<?> type, Class<?> objectType)
	{
		if (type == null)
			throw new NullPointerException("type");
		if (objectType == null)
			throw new NullPointerException("objectType");
// #if WINRT
		// if (type.GetTypeInfo().IsAssignableFrom(objectType.GetTypeInfo()))
		// return true;
// #else
		if (type.isAssignableFrom(objectType))
			return true;
// #endif
		return false;
	}

}
