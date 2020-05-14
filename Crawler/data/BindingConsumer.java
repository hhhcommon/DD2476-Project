15
https://raw.githubusercontent.com/Florizt/RxMVVM/master/rxmvvmlib/src/main/java/com/rx/rxmvvmlib/binding/command/BindingConsumer.java
package com.rx.rxmvvmlib.binding.command;

/**
 * A one-argument action.
 *
 * @param <T> the first argument type
 */
public interface BindingConsumer<T> {
    void call(T t);
}
