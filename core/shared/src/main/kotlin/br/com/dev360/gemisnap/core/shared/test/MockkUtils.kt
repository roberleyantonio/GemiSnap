package br.com.dev360.gemisnap.core.shared.test

import io.mockk.MockKVerificationScope
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify

fun verifyNever(verifyBlock: MockKVerificationScope.() -> Unit) = verify(exactly = 0, verifyBlock = verifyBlock)
fun verifyOnce(verifyBlock: MockKVerificationScope.() -> Unit) = verify(exactly = 1, verifyBlock = verifyBlock)

fun coVerifyNever(verifyBlock: suspend  MockKVerificationScope.() -> Unit) = coVerify(exactly = 0, verifyBlock = verifyBlock)
fun coVerifyOnce(verifyBlock: suspend  MockKVerificationScope.() -> Unit) = coVerify(exactly = 1, verifyBlock = verifyBlock)

inline fun <reified T : Any> relaxedMock(block: T.() -> Unit = {}) = mockk(relaxed = true, block = block)