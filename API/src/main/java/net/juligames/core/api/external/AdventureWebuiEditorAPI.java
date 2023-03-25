package net.juligames.core.api.external;
/*
 * This file is part of adventure-webui, licensed under the MIT License.
 *
 * Copyright (c) 2021 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import net.juligames.core.api.API;
import net.juligames.core.api.message.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The adventure-webui editor API.
 */
public final class AdventureWebuiEditorAPI {
    public static final URI JULIGAMES_API_PRODUCTION;
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\{\"token\" *: *\"(.*?)\"");
    @TestOnly
    public static URI JULIGAMES_API_DEVELOPMENT_A;
    @TestOnly
    public static URI JULIGAMES_API_DEVELOPMENT_B;

    @TestOnly
    public static URI JULIGAMES_API_DEVELOPMENT_LOCAL;

    static {
        try {
            JULIGAMES_API_PRODUCTION = new URI("https://editor.juligames.net");
            //JULIGAMES_API_DEVELOPMENT_A = new URI("censored");
            //JULIGAMES_API_DEVELOPMENT_B = new URI("censored");
            JULIGAMES_API_DEVELOPMENT_LOCAL = new URI("https://localhost");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private final URI root;
    private final HttpClient client;

    /**
     * Creates a new instance of the editor API with the given root URI.
     *
     * @param root the root URI
     */
    public AdventureWebuiEditorAPI(final @NotNull URI root) {
        this(root, HttpClient.newHttpClient());
    }
    /**
     * Creates a new instance of the editor API with the default JuliGames api
     */
    public AdventureWebuiEditorAPI() {
        this(JULIGAMES_API_PRODUCTION, HttpClient.newHttpClient());
    }

    /**
     * Creates a new instance of the editor API with the given root URI and a client.
     *
     * @param root   the root URI
     * @param client the client
     */
    public AdventureWebuiEditorAPI(final @NotNull URI root, final @NotNull HttpClient client) {
        this.root = Objects.requireNonNull(root, "root");
        this.client = Objects.requireNonNull(client, "client");
    }

    /**
     * Creates a new instance of the editor API with the default JuliGames api and a default client.
     *
     * @param client the client
     */
    public AdventureWebuiEditorAPI(final @NotNull HttpClient client) {
        this.root = Objects.requireNonNull(JULIGAMES_API_PRODUCTION, "root");
        this.client = Objects.requireNonNull(client, "client");
    }

    public static void main(String[] args) {
        AdventureWebuiEditorAPI api = new AdventureWebuiEditorAPI();
        String app = api.startSessionAndGenerateLink("<rainbow>hsasdhjasdasldjha", "/rainbow", "app");
        System.out.println(app);
    }

    public static void maidn(String[] args) {
        AdventureWebuiEditorAPI api = new AdventureWebuiEditorAPI();
        CompletableFuture<String> juliGamesCore = api.retrieveSession("6bd7388acd3a4743867dfbd02debe9ff");
        try {
            System.out.println(juliGamesCore.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts a session, returning the token.
     *
     * @param input       the input
     * @param command     the command
     * @param application the application name
     * @return a completable future that will provide the token
     */
    public @NotNull CompletableFuture<String> startSession(final @NotNull String input, final @NotNull String command, final @NotNull String application) {
        final HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(constructBody(input, command, application))).uri(root.resolve(URI.create("/api/editor/input"))).build();
        final CompletableFuture<String> result = new CompletableFuture<>();

        this.client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(stringHttpResponse -> {
            if (stringHttpResponse.statusCode() != 200) {
                result.completeExceptionally(new IOException("The server could not handle the request."));
            } else {
                final String body = stringHttpResponse.body();
                final Matcher matcher = TOKEN_PATTERN.matcher(body);

                if (matcher.find()) {
                    final String group = matcher.group(1);
                    result.complete(group);
                    return result;
                }

                result.completeExceptionally(new IOException("The result did not contain a token."));
            }
            return null;
        });

        return result;
    }

    /**
     * Retrieves the result of a session, given a token.
     *
     * @param token the token
     * @return the resulting MiniMessage string in a completable future
     */
    public @NotNull CompletableFuture<String> retrieveSession(final @NotNull String token) {
        final HttpRequest request = HttpRequest.newBuilder().GET().uri(root.resolve(URI.create("/api/editor/output?token=" + token))).build();
        final CompletableFuture<String> result = new CompletableFuture<>();

        this.client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(stringHttpResponse -> {
            final int statusCode = stringHttpResponse.statusCode();
            if (statusCode == 404) {
                result.complete(null);
            } else if (statusCode != 200) {
                result.completeExceptionally(new IOException("The server could not handle the request."));
            } else {
                result.complete(stringHttpResponse.body());
            }
            return null;
        });

        return result;
    }

    @Contract(pure = true)
    public @NotNull String generateLink(String token) {
        return root.toString() + "?token=" + token;
    }

    /**
     * @param input   initial miniMessage
     * @param command command with {token}
     * @param app     app name
     * @return the link
     */
    @Contract(pure = true)
    public @NotNull String startSessionAndGenerateLink(String input, String command, String app) {
        try {
            return generateLink(startSession(input, command, app).get(10, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param command command with {token}
     * @return the link
     */
    @Contract(pure = true)
    public @NotNull String startSessionAndGenerateLink(String command) {
        try {
            return generateLink(startSession("", command, API.get().getVersion()).get(10, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param command command with {token}
     * @param app app
     * @return the link
     */
    @Contract(pure = true)
    public @NotNull String startSessionAndGenerateLink(String command, String app) {
        try {
            return generateLink(startSession("", command, app).get(10, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param message the initial message
     * @param command command with {token}
     * @param app app
     * @return the link
     */
    @Contract(pure = true)
    public @NotNull String startSessionAndGenerateLink(@NotNull Message message, String command, String app) {
        try {
            return generateLink(startSession(message.getMiniMessage(), command, app).get(10, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private @NotNull String constructBody(final @NotNull String input, final @NotNull String command, final @NotNull String application) {
        return String.format("{\"input\":\"%s\",\"command\":\"%s\",\"application\":\"%s\"}", input, command, application);
    }
}