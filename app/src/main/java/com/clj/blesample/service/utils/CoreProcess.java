
package com.clj.blesample.service.utils;


/**
 * The type Core process.
 */
public class CoreProcess {

    /**
     * The interface On status listener.
     */
    public interface OnStatusListener {
        /**
         * On ready.
         */
        void onReady();

        /**
         * On not ready.
         */
        void onNotReady();

        /**
         * On timeout.
         */
        void onTimeout();
    }

    /**
     * 处理状态监听函数
     */
    private OnStatusListener mListener;

    /**
     * Sets on status listener.
     *
     * @param listener the listener
     */
    public void setOnStatusListener(OnStatusListener listener) {
        this.mListener = listener;
    }
}
