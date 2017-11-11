package pw.hshen.hrpc.client;

import lombok.extern.slf4j.Slf4j;
import pw.hshen.hrpc.common.model.RPCResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author hongbin
 * Created on 11/11/2017
 */
@Slf4j
public class RPCResponseFuture implements Future<Object> {

	private RPCResponse response;

	CountDownLatch latch = new CountDownLatch(1);

	public void done(RPCResponse response) {
		this.response = response;
		latch.countDown();
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public RPCResponse get() throws InterruptedException, ExecutionException {
		try {
			latch.await();
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		return response;
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		try {
			if (!latch.await(timeout, unit)) {
				throw new TimeoutException("RPC Request timeout!");
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		return response;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCancelled() {
		throw new UnsupportedOperationException();
	}
}