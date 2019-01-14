package jMono_Framework;

import jMono_Framework.dotNet.IServiceProvider;
import jMono_Framework.utilities.ReflectionHelpers;

import java.util.HashMap;

public class GameServiceContainer implements IServiceProvider
{
    HashMap<Class<?>, Object> services;

    public GameServiceContainer()
    {
        services = new HashMap<Class<?>, Object>();
    }

    public void addService(Class<?> type, Object provider)
    {
        if (type == null)
            throw new NullPointerException("type");
        if (provider == null)
            throw new NullPointerException("provider");
        if (!ReflectionHelpers.isAssignableFrom(type, provider))
            throw new IllegalArgumentException("The provider does not match the specified service type!");

        services.put(type, provider);
    }

    public Object getService(Class<?> type)
    {
        if (type == null)
            throw new NullPointerException("type");

        // Object service;
        // if (services.tryGetValue(type, out service))
        // return service;

        // return null;
        return services.get(type);
    }

    public void removeService(Class<?> type)
    {
        if (type == null)
            throw new NullPointerException("type");

        services.remove(type);
    }

    public <T> void addService(T provider)
    {
        addService(provider.getClass(), provider);
    }

    // TODO: How can I implement this behavior
    /*
     * public <T> T getService() // where T : class
     * {
     * Object service = getService(typeof(T));
     * 
     * if (service == null)
     * return null;
     * 
     * return (T)service;
     * }
     */
}
