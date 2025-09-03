package com.sesac.solbid.exception;

public class PointNotFoundException extends CustomException {
    public PointNotFoundException() {
        super(ErrorCode.POINT_NOT_FOUND);
    }

}