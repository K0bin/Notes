package k0bin.notes.util;


import android.arch.core.util.Function;
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
					callback.callback.apply(callback.args);
				}
			}
		};
		executor = Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors());
	}

	public static <T, R> void runAsync(T args, Function<T, R> async, Function<CallbackArgs<T, R>, Void> callback) {
		if (executor == null || handler == null) {
			throw new RuntimeException("AsyncHelper has not yet been initialized.");
		}

		if (async == null) {
			throw new IllegalArgumentException("async must not be null.");
		}

		executor.submit(() -> {
			R result = async.apply(args);
			Message message = handler.obtainMessage(TASK_COMPLETE, new CallbackInvokation(new CallbackArgs<>(args, result), callback));
			message.sendToTarget();
		});
	}

	public static <T> void runAsync(T args, Function<T, Void> async) {
		runAsync(args, async, null);
	}

	public static void runAsync(Runnable async) {
		runAsync(null, arg -> { async.run(); return null; });
	}

	private static class CallbackArgs<T, R> {
		private T args;
		private R result;

		private CallbackArgs(T args, R result) {
			this.args = args;
			this.result = result;
		}
	}

	private static class CallbackInvokation<T, R> {
		private CallbackArgs<T, R> args;
		private Function<CallbackArgs<T, R>, Void> callback;

		private CallbackInvokation(CallbackArgs<T, R> args, Function<CallbackArgs<T, R>, Void> callback) {
			this.args = args;
			this.callback = callback;
		}
	}
}
