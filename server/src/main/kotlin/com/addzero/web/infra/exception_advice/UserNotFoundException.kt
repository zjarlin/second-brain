package com.addzero.web.infra.exception_advice


class UserNotFoundException : BizException(ErrorEnum.USER_NOT_FIND)

class UsernameOrPasswordException : BizException(ErrorEnum.USERNAME_OR_PASSWORD_ERROR)

class UsernameOrEmailAlreadyExistsException : BizException(ErrorEnum.USERNAME_OR_EMAIL_ALREADY_EXISTS)

class CodeIsNotExistsException : BizException(ErrorEnum.EMAIL_CODE_IS_NOT_EXIST)

class CodeIsNotTrueException : BizException(ErrorEnum.EMAIL_CODE_IS_NOT_TRUE)

class OldPasswordException : BizException(ErrorEnum.OLD_PASSWORD_IS_NOT_TRUE)