package com.gnefedev.javaee.testing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by gerakln on 21.08.16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TestResponse<T extends Throwable> {
    private TestStatus status = TestStatus.SUCCESS;
    @XmlJavaTypeAdapter(ThrowableAdapter.class)
    private T error = null;
    private String sessionId = null;

    public TestResponse() {
    }

    public TestResponse(TestStatus status, T error) {
        this.status = status;
        this.error = error;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public T getError() {
        return error;
    }

    public void setError(T error) {
        this.error = error;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
