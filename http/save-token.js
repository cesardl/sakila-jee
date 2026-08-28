// Shared response handler for POST /auth/token, referenced as `> ./save-token.js`.
//
// The point of asserting here rather than just grabbing the token: on a failed login
// `accessToken` is undefined, and storing that silently poisons every following request
// in the file, which then fails with a 401 that looks like an authorization bug instead
// of a login bug.

// IntelliJ returns response.body already parsed on some versions and as a raw JSON
// string on others. Normalising here means the .http files do not care which.
var body = typeof response.body === "string" ? JSON.parse(response.body) : response.body;

client.test("login returns 200", function () {
    client.assert(response.status === 200,
        "login failed with " + response.status + ": " + JSON.stringify(body));
});

client.test("response is a bearer token", function () {
    client.assert(body.tokenType === "Bearer", "tokenType is not Bearer");
    client.assert(body.expiresIn > 0, "expiresIn is not positive");

    var token = body.accessToken;
    client.assert(typeof token === "string" && token.split(".").length === 3,
        "accessToken is not a three-part JWT");
});

// Global, so it survives across files: run this once and actors/films/customers.http
// can be run on their own.
client.global.set("auth_token", body.accessToken);
