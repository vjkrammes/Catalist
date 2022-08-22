package solutions.vjk.catalist.models

import solutions.vjk.catalist.enums.RepositoryResultCode

@Suppress("unused")
class RepositoryResult(
    private val code: RepositoryResultCode = RepositoryResultCode.SUCCESS,
    private val message: String? = null,
    private val messages: Array<String>? = null
) {
    constructor() : this(RepositoryResultCode.SUCCESS, null, null)
    constructor(code: RepositoryResultCode) : this(code, null, null)
    constructor(message: String) : this(RepositoryResultCode.SEEMESSAGE, message, null)
    constructor(messages: Array<String>?) : this(RepositoryResultCode.SEEMESSAGE, null, messages)
    constructor(message: String, messages: Array<String>?) : this(
        RepositoryResultCode.SEEMESSAGE,
        message,
        messages
    )

    constructor(code: RepositoryResultCode, message: String?) : this(code, message, null)
    constructor(code: RepositoryResultCode, messages: Array<String>?) : this(code, null, messages)

    fun getCode(): RepositoryResultCode = code
    fun getMessage(): String = message ?: ""
    fun getMessages(): Array<String> = messages ?: emptyArray()

    val isSuccessResult: Boolean =
        code == RepositoryResultCode.SUCCESS && message.isNullOrEmpty() && messages.isNullOrEmpty()

    companion object {
        fun duplicate(message: String) = RepositoryResult(RepositoryResultCode.DUPLICATE, message)
        fun fromException(ex: Exception) =
            RepositoryResult(RepositoryResultCode.EXCEPTION, ex.message)

        fun invalid(message: String) =
            RepositoryResult(RepositoryResultCode.INVALIDPARAMETERS, message)

        fun missing(message: String) = RepositoryResult(RepositoryResultCode.MISSINGDATA, message)
        fun notFound(message: String) = RepositoryResult(RepositoryResultCode.NOTFOUND, message)
        fun success() = RepositoryResult()
    }
}