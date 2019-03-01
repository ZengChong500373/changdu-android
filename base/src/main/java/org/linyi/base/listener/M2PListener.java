package org.linyi.base.listener;

public interface M2PListener<T> {
     void onLoadSuccess(T t);
     void onLoadFaile();
}
