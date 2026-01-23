/**
 * Use cases of the Auth module.
 *
 * Implementations of primary ports:
 * - AuthenticateUserUseCaseImpl: authenticates user and generates JWT token
 * - RefreshTokenUseCaseImpl: refreshes access token using a valid refresh token
 *
 * These use cases orchestrate authentication flows by coordinating
 * security adapters (AuthenticationPort, TokenPort) without containing
 * business rules or infrastructure concerns.
 */
package com.apis.fintrack.application.auth;


