# JWT, Keycloak, and OAuth 2.0 Overview

## JSON Web Token (JWT)

JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. It is commonly used for authentication and authorization purposes in web applications.

### Core Concepts

| Concept             | Description                                                                                                                |
|---------------------|----------------------------------------------------------------------------------------------------------------------------|
| Token               | A compact and URL-safe string representing claims that can be transmitted between parties.                                  |
| Claims              | Pieces of information asserted about a subject. Common claims include `iss` (issuer), `sub` (subject), `exp` (expiration), and `aud` (audience). |
| Header              | Contains metadata about the type of token and the signing algorithm used.                                                   |
| Payload (Claims)    | Contains the actual claims about the subject, such as user ID, roles, and permissions.                                      |
| Signature           | Used to verify the integrity of the token and to ensure it has not been tampered with.                                      |

### Usage

1. **Authentication**: JWTs can be used as tokens for authenticating users. Upon successful authentication, a JWT is issued to the client, which can then be included in subsequent requests.
2. **Authorization**: JWTs can contain user roles and permissions, allowing servers to authorize access to certain resources based on the claims present in the token.
3. **Stateless Sessions**: JWTs are stateless, meaning servers do not need to store session data. All necessary information is contained within the token itself.
4. **Interoperability**: JWTs can be easily exchanged between different platforms and technologies due to their standardized format.

## OAuth 2.0

OAuth 2.0 is an authorization framework that enables third-party applications to obtain limited access to a user's resources without exposing their credentials. It is widely used for delegated authorization in web and mobile applications.

### Core Concepts

| Concept             | Description                                                                                                                   |
|---------------------|-------------------------------------------------------------------------------------------------------------------------------|
| Client              | An application requesting access to protected resources on behalf of the resource owner (user).                               |
| Resource Owner      | An entity capable of granting access to a protected resource. Typically, this is the end user.                                 |
| Authorization Server | Manages the authentication and authorization of clients and users, issuing access tokens after successful authentication.       |
| Resource Server     | Server hosting the protected resources that the client wants to access.                                                       |
| Access Token        | A credential representing the authorization granted to the client by the resource owner.                                     |
| Grant Types         | Different methods for obtaining an access token. Common grant types include Authorization Code, Implicit, Client Credentials, and Resource Owner Password Credentials. |

### Usage

1. **Authentication**: OAuth 2.0 enables users to grant third-party applications limited access to their resources without sharing their credentials.
2. **Authorization**: Clients obtain access tokens from the authorization server, which are then presented to the resource server to access protected resources.
3. **Scalability**: OAuth 2.0 is highly scalable and can be used in various scenarios, including web, mobile, and IoT applications.
4. **Security**: OAuth 2.0 provides mechanisms for securing communication between clients, authorization servers, and resource servers, reducing the risk of unauthorized access.

## Keycloak

Keycloak is an open-source Identity and Access Management (IAM) solution developed by Red Hat. It provides features such as single sign-on (SSO), identity brokering, and user federation.

### Core Concepts

| Concept             | Description                                                                                                                   |
|---------------------|-------------------------------------------------------------------------------------------------------------------------------|
| Realm               | A security domain where all authentication, authorization, and user management configurations are stored.                    |
| User                | Represents an individual who can authenticate and access resources within the Keycloak realm.                                 |
| Client              | A service or application that can request authentication and authorization on behalf of a user.                               |
| Identity Provider   | External systems (e.g., LDAP, Active Directory, social media platforms) that Keycloak can integrate with for user authentication. |
| Role                | Defines a set of permissions that can be assigned to users or groups to control access to resources.                          |

### Usage

1. **Single Sign-On (SSO)**: Keycloak allows users to log in once and access multiple applications without being prompted to log in again.
2. **Identity Brokering**: Integration with external identity providers enables Keycloak to authenticate users using credentials from those providers.
3. **User Federation**: Keycloak can federate with external user stores, such as LDAP or Active Directory, to manage user identities centrally.
4. **Authorization Services**: Provides fine-grained access control using roles and policies, allowing administrators to define who can access what resources.

## Relationship between Keycloak and OAuth 2.0

Keycloak implements OAuth 2.0 alongside other authentication and authorization protocols. It acts as both an OAuth 2.0 Authorization Server and an OpenID Connect (OIDC) Identity Provider, enabling developers to integrate OAuth 2.0-based authentication and authorization mechanisms into their applications. Additionally, Keycloak offers various features beyond OAuth 2.0, such as Single Sign-On (SSO), user federation, and role-based access control.

In summary, while Keycloak is not solely an OAuth 2.0 implementation, it does include robust support for OAuth 2.0 as part of its broader identity and access management capabilities.