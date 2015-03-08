package com.droiddevil.deviltools.service;

import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public abstract class ServiceConnectionManager<T extends Service> {

    public interface ServiceConnectionListener<T> {
        void onServiceConnected(T service);

        void onServiceDisconnected();
    }

    public interface ServiceCommand<T> {
        void runCommand(T service);
    }

    private Context mContext;

    private T mService;

    private final Set<ServiceConnectionListener<T>> mServiceConnectionListeners = new LinkedHashSet<ServiceConnectionListener<T>>();

    private final Queue<ServiceCommand<T>> mCommandQueue = new ConcurrentLinkedQueue<ServiceCommand<T>>();

    public ServiceConnectionManager(Context context) {
        mContext = context;
    }

    public void start() {
        Intent intent = new Intent(mContext, getServiceClass());
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stop() {
        mContext.unbindService(mServiceConnection);
    }

    public void addServiceConnectionListener(ServiceConnectionListener<T> listener) {
        mServiceConnectionListeners.add(listener);
    }

    public void removeServiceConnectionListener(ServiceConnectionListener<T> listener) {
        mServiceConnectionListeners.remove(listener);
    }

    public void runServiceCommand(ServiceCommand<T> command) {
        if (mService != null) {
            command.runCommand(mService);
        } else {
            mCommandQueue.offer(command);
        }
    }

    public T getService() {
        return mService;
    }

    protected abstract Class<T> getServiceClass();

    protected abstract T getService(IBinder binder);

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = getService(service);

            // Notify service connection listeners
            for (ServiceConnectionListener<T> listener : mServiceConnectionListeners) {
                listener.onServiceConnected(mService);
            }

            // Run pending commands
            ServiceCommand<T> command = mCommandQueue.poll();
            while (command != null) {
                command.runCommand(mService);
                command = mCommandQueue.poll();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mService = null;

            // Notify service connection listeners
            for (ServiceConnectionListener<T> listener : mServiceConnectionListeners) {
                listener.onServiceDisconnected();
            }
        }
    };

}
