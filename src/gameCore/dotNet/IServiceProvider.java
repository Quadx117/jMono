package gameCore.dotNet;

public interface IServiceProvider {

	Object getService(Class<?> serviceType);
}
