package k0bin.notes.util;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unchecked")
public final class AsyncHelper {
	private static ExecutorService executor;
	private static Handler handler;
	private static final int TASK_COMPLETE = 1;
	private AsyncHelper() {
	}

	public static void init() {
		handler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what != TASK_COMPLETE) {
					return;
				}

				CallbackInvokation callback = (CallbackInvokation) msg.obj;
				if (callback.callback != null) {
					callback.callback.run(callback.result);
				}
			}
		};
		executor = Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors());
	}

	public static <R> void runAsync(ReturnRunnable<R> async, ArgumentRunnable<R> callback) {
		if (executor == null || handler == null) {
			throw new RuntimeException("AsyncHelper has not yet been initialized.");
		}

		if (async == null) {
			throw new IllegalArgumentException("async must not be null.");
		}

		executor.submit(() -> {
		    try {
                R result = async.run();
                if (callback != null) {
                    Message message = handler.obtainMessage(TASK_COMPLETE, new CallbackInvokation(result, callback));
                    message.sendToTarget();
                }
            } catch (Exception e) {
		        throw new RuntimeException(e);
            }
		});
	}

	public static void runAsync(Runnable async) {
		runAsync(() -> { async.run(); return null; }, null);
	}

	private static class CallbackInvokation<R> {
		private R result;
		private ArgumentRunnable<R> callback;

		private CallbackInvokation(R result, ArgumentRunnable<R> callback) {
			this.result = result;
			this.callback = callback;
		}
	}

	public interface ArgumentRunnable<T> {
	    void run(T arg);
    }

    public interface ReturnRunnable<R> {
        R run();
    }
}
