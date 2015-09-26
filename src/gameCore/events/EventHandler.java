package gameCore.events;

import java.util.function.BiConsumer;

public interface EventHandler<TEventArgs extends EventArgs> extends BiConsumer<Object, TEventArgs> {

}
