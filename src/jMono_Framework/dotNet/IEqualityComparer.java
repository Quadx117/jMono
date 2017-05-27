package jMono_Framework.dotNet;

public interface IEqualityComparer<T>
{
	boolean equals(T x, T y);
	int hashCode(T obj);
}
