# TwitteerCopy - Microblog Serverless

TwitteerCopy es una plataforma de microblog estilo Twitter donde los usuarios pueden registrarse, autenticarse y publicar mensajes de hasta 140 caracteres. El sistema utiliza una arquitectura escalable basada en microservicios en AWS Lambda, con autenticación segura mediante JWT emitido por AWS Cognito.

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Instalación y Despliegue](#instalación-y-despliegue)
- [Endpoints](#endpoints)
- [Flujo de Autenticación](#flujo-de-autenticación)
- [Microservicios](#microservicios)
- [Configuración CORS y Seguridad](#configuración-cors-y-seguridad)
- [Base de Datos](#base-de-datos)
- [Contacto](#contacto)

---

## 🎯 Descripción General

TwitteerCopy comenzó como un monolito en Spring Boot y ha evolucionado hacia una arquitectura de microservicios serverless. Los componentes principales son:

- **Frontend**: JavaScript alojado en GitHub Pages o S3 público
- **Autenticación**: AWS Cognito con tokens JWT
- **Backend**: Funciones AWS Lambda expuestas mediante API Gateway
- **Base de datos**: PostgreSQL (monolito) o DynamoDB (microservicios)

Cada usuario puede:
- Registrarse y autenticarse de forma segura
- Actualizar su perfil
- Crear posts de hasta 140 caracteres
- Ver el feed completo ordenado por fecha

---

## 🏗️ Arquitectura

### Flujo General

```
┌─────────────────────────────────────────────────────────────┐
│                     FRONTEND (GitHub Pages/S3)              │
│              JavaScript + Forms + CORS requests             │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                     AWS COGNITO                             │
│         Autenticación, JWT tokens, OAuth 2.0                │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                   API GATEWAY                               │
│         Enrutamiento de solicitudes HTTP/HTTPS              │
└─┬───────────────────────────────────────────────────────────┘
  │
  ├─────────────────────┬─────────────────────┬──────────────┐
  │                     │                     │              │
  ▼                     ▼                     ▼              ▼
┌──────────┐    ┌──────────────┐    ┌──────────────┐   ┌──────────┐
│Lambda:  │    │Lambda:       │    │Lambda:       │   │Database │
│Usuarios │    │Posts         │    │Stream        │   │(DynamoDB│
│         │    │              │    │              │   │or        │
│- Crear  │    │- Crear posts │    │- Feed        │   │PostgreSQL)
│- Actualizar   │- Validar     │    │- Ordenar     │   │         │
│- Obtener info │  JWT         │    │- Agregar     │   │         │
│- JWT          │              │    │              │   │         │
└──────────┘    └──────────────┘    └──────────────┘   └──────────┘
```

### Componentes Clave

| Componente | Descripción | Tecnología |
|---|---|---|
| **Frontend** | Interfaz de usuario y formularios | JavaScript vanilla, HTML, CSS |
| **API Gateway** | Enrutador y gestor de solicitudes HTTP | AWS API Gateway |
| **AWS Cognito** | Autenticación y gestión de usuarios | OAuth 2.0, JWT |
| **Lambda - Usuarios** | Gestión de perfiles de usuario | Python/Node.js/Java |
| **Lambda - Posts** | CRUD de posts individuales | Python/Node.js/Java |
| **Lambda - Stream** | Feed de todos los posts | Python/Node.js/Java |
| **Base de Datos** | Almacenamiento persistente | DynamoDB / PostgreSQL |

---

## ⚙️ Requisitos

### Software y Servicios

- **AWS Account** con acceso a: Lambda, API Gateway, Cognito, DynamoDB/RDS
- **Node.js** (v14+) para desarrollo local
- **Python** o **Java** (según lenguaje de Lambda)
- **Git** para control de versiones
- **AWS CLI** configurada con credenciales

### Configuración de AWS Cognito

- User Pool creado con dominio personalizado
- App client configurado con:
  - Authorization code flow habilitado
  - Redirect URIs apuntando al frontend
  - Logout URIs configuradas
  - CORS habilitado para el dominio del frontend

---

## 🚀 Instalación y Despliegue

### 1. Clonar el Repositorio

```bash
git clone https://github.com/XxELIngexX/AREP-T7
cd AREP-T7
```

### 2. Configurar Variables de Entorno

Crear un archivo `.env` en la raíz del proyecto:

```env
# AWS Configuration
AWS_REGION=us-east-1
AWS_ACCOUNT_ID=123456789012

# Cognito
COGNITO_USER_POOL_ID=us-east-1_xxxxx
COGNITO_CLIENT_ID=client_id_xxxx
COGNITO_DOMAIN=https://twitteer-auth.auth.us-east-1.amazoncognito.com

# API Gateway
API_BASE_URL=https://api-xxxxx.execute-api.us-east-1.amazonaws.com/

# Frontend
FRONTEND_URL= https://xxelingexx.github.io/AREP-T7-Front/
# o
FRONTEND_URL=https://twitter-Copy.s3.us-east-1.amazonaws.com

# Database
DB_HOST=database-instance.xxxxx.us-east-1.rds.amazonaws.com
DB_NAME=postgres
DB_USER=admin
DB_PASSWORD=secure_password
```

### 3. Desplegar Funciones Lambda

```bash
# Navegar a la carpeta de lambdas
cd backend/lambda

# Desplegar servicio de usuarios
cd usuarios && sam deploy --guided && cd ..

# Desplegar servicio de posts
cd posts && sam deploy --guided && cd ..

# Desplegar servicio de stream
cd stream && sam deploy --guided && cd ..
```

### 4. Desplegar Frontend

#### Opción A: GitHub Pages

```bash
cd frontend

# Actualizar VITE_API_URL en .env.production
npm run build

# Subir archivos a rama gh-pages
git add dist/
git commit -m "Deploy to GitHub Pages"
git push origin main
```

#### Opción B: AWS S3

```bash
cd frontend
npm run build

aws s3 sync dist/ s3://my-twitteer-bucket/ --delete

# Opcional: Configurar CloudFront para HTTPS
```

### 5. Configurar API Gateway

- Crear recursos `/api/users`, `/api/posts`, `/api/stream`
- Vincular métodos HTTP (GET, POST) con funciones Lambda
- Habilitar CORS en cada método
- Configurar autorizadores JWT en Cognito

---

## 📡 Endpoints

Todos los endpoints requieren un header `Authorization: Bearer {jwt_token}` excepto `/auth/callback`.

### Autenticación

| Endpoint | Método | Descripción | JWT Requerido |
|---|---|---|---|
| `/auth/callback` | `GET` | Intercambia `code` por tokens de Cognito | ❌ No |
| `/auth/logout` | `GET` | Invalida sesión y redirige al login | ✅ Sí |

### Usuarios

| Endpoint | Método | Descripción | JWT Requerido |
|---|---|---|---|
| `/api/users` | `POST` | Crear o actualizar perfil de usuario | ✅ Sí |
| `/api/users/me` | `GET` | Obtener información del usuario autenticado | ✅ Sí |

**POST /api/users**
```json
{
  "username": "juan_perez",
  "email": "juan@example.com",
  "bio": "Desarrollador de software"
}
```

Respuesta (201 Created):
```json
{
  "id": "user_123",
  "username": "juan_perez",
  "email": "juan@example.com",
  "bio": "Desarrollador de software",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**GET /api/users/me**

Respuesta (200 OK):
```json
{
  "id": "user_123",
  "username": "juan_perez",
  "email": "juan@example.com",
  "bio": "Desarrollador de software",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### Posts

| Endpoint | Método | Descripción | JWT Requerido |
|---|---|---|---|
| `/api/posts` | `POST` | Crear un nuevo post | ✅ Sí |
| `/api/posts` | `GET` | Obtener todos los posts (stream) | ✅ Sí |

**POST /api/posts**
```json
{
  "content": "¡Hola Twitteer! Este es mi primer post con menos de 140 caracteres."
}
```

Respuesta (201 Created):
```json
{
  "id": "post_456",
  "userId": "user_123",
  "username": "juan_perez",
  "content": "¡Hola Twitteer! Este es mi primer post con menos de 140 caracteres.",
  "createdAt": "2024-01-15T11:45:00Z"
}
```

**GET /api/posts**

Respuesta (200 OK):
```json
{
  "posts": [
    {
      "id": "post_789",
      "userId": "user_456",
      "username": "maria_lopez",
      "content": "Compartiendo mi experiencia en la nube",
      "createdAt": "2024-01-15T12:00:00Z"
    },
    {
      "id": "post_456",
      "userId": "user_123",
      "username": "juan_perez",
      "content": "¡Hola Twitteer! Este es mi primer post.",
      "createdAt": "2024-01-15T11:45:00Z"
    }
  ],
  "total": 2
}
```

---

## 🔐 Flujo de Autenticación

### Paso 1: Usuario inicia sesión

El usuario hace clic en "Login" en el frontend, que lo redirige a AWS Cognito:

```
Frontend → AWS Cognito
GET https://twitteer-auth.auth.us-east-1.amazoncognito.com/oauth2/authorize
  ?client_id=client_id_xxxx
  &response_type=code
  &scope=openid+email+profile
  &redirect_uri=https://frontend.com/callback
```

### Paso 2: Cognito autentica al usuario

El usuario ingresa credenciales. Si son válidas, Cognito redirige al frontend con un `authorization code`:

```
AWS Cognito → Frontend
GET https://frontend.com/callback
  ?code=authorization_code_xxxxx
  &state=random_state_value
```

### Paso 3: Frontend intercambia code por tokens

El frontend (o backend) envía el `code` a Cognito para obtener tokens:

```javascript
// En el backend o frontend seguro
const response = await fetch('https://twitteer-auth.auth.us-east-1.amazoncognito.com/oauth2/token', {
  method: 'POST',
  headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
  body: new URLSearchParams({
    grant_type: 'authorization_code',
    code: 'authorization_code_xxxxx',
    client_id: 'client_id_xxxx',
    client_secret: 'client_secret_xxxx',
    redirect_uri: 'https://frontend.com/callback'
  })
});

const data = await response.json();
// data.access_token
// data.id_token
// data.refresh_token
```

### Paso 4: Frontend almacena tokens

```javascript
localStorage.setItem('accessToken', data.access_token);
localStorage.setItem('idToken', data.id_token);
localStorage.setItem('refreshToken', data.refresh_token);
```

### Paso 5: Frontend realiza solicitudes autenticadas

Cada solicitud a la API incluye el JWT en el header:

```javascript
const response = await fetch('https://api.example.com/api/users/me', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
  }
});
```

### Paso 6: API valida JWT

Lambda valida el JWT con Cognito:

```python
import json
import boto3
from jose import jwt, JWTClaimsError

cognito_client = boto3.client('cognito-idp')

def validate_jwt(token):
    try:
        # Decodificar y validar JWT
        decoded = jwt.get_unverified_claims(token)
        return decoded
    except (JWTClaimsError, Exception) as e:
        return None
```

### Paso 7: Logout

```
Frontend → AWS Cognito
GET https://twitteer-auth.auth.us-east-1.amazoncognito.com/logout
  ?client_id=client_id_xxxx
  &logout_uri=https://frontend.com/logout
```

Cognito invalida la sesión y redirige al frontend.

---

## 🔧 Microservicios

### Estructura de Directorios

```
backend/
├── lambda/
│   ├── usuarios/
│   │   ├── handler.py (o index.js)
│   │   ├── requirements.txt (o package.json)
│   │   └── template.yaml
│   ├── posts/
│   │   ├── handler.py
│   │   ├── requirements.txt
│   │   └── template.yaml
│   └── stream/
│       ├── handler.py
│       ├── requirements.txt
│       └── template.yaml
├── shared/
│   └── jwt_validator.py
└── Makefile
```

### Lambda: Servicio de Usuarios

**Responsabilidades:**
- Crear/actualizar perfiles de usuario
- Obtener información del usuario autenticado
- Validar JWT

**Eventos:**
- `POST /api/users` → Crear usuario
- `GET /api/users/me` → Obtener usuario actual

### Lambda: Servicio de Posts

**Responsabilidades:**
- Crear nuevos posts (validar longitud máxima: 140 caracteres)
- Validar JWT
- Almacenar en base de datos

**Eventos:**
- `POST /api/posts` → Crear post

### Lambda: Servicio de Stream

**Responsabilidades:**
- Consultar todos los posts
- Ordenar por fecha (más reciente primero)
- Agrupar por usuario
- Validar JWT

**Eventos:**
- `GET /api/posts` → Obtener feed completo

---

## 🛡️ Configuración CORS y Seguridad

### CORS en API Gateway

Para cada método en API Gateway, configurar respuestas de CORS:

```json
{
  "Access-Control-Allow-Headers": "Content-Type,X-Amz-Date,Authorization,X-Api-Key",
  "Access-Control-Allow-Methods": "GET,POST,OPTIONS,DELETE,PUT",
  "Access-Control-Allow-Origin": "https://usuario.github.io"
}
```

### Configuración en Spring Boot (si aplica)

```java
@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("https://usuario.github.io", "http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
```

### Validación JWT

Todas las rutas `/api/**` requieren un JWT válido. Configurar autorizador en API Gateway:

```json
{
  "IdentitySource": "method.request.header.Authorization",
  "AuthorizerUri": "arn:aws:apigateway:us-east-1:lambda:path/2015-03-31/functions/arn:aws:lambda:us-east-1:123456789012:function:JWTValidator/invocations",
  "AuthorizerResultTtlInSeconds": 300,
  "Type": "TOKEN"
}
```

### Headers de Seguridad

```
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
Strict-Transport-Security: max-age=31536000; includeSubDomains
Content-Security-Policy: default-src 'self'
```

---

## 💾 Base de Datos

### PostgreSQL (Monolito)

**Tablas:**

```sql
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  bio TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE posts (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  content VARCHAR(140) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_posts_user_id ON posts(user_id);
CREATE INDEX idx_posts_created_at ON posts(created_at DESC);
```

### DynamoDB (Microservicios)

**Tabla: Users**

```
PrimaryKey: userId (String)
Attributes:
  - username (String, GSI)
  - email (String)
  - bio (String)
  - createdAt (Number, timestamp)
```

**Tabla: Posts**

```
PrimaryKey: postId (String)
SortKey: createdAt (Number)
Attributes:
  - userId (String, GSI)
  - username (String)
  - content (String)
  - createdAt (Number, timestamp)

GSI: userId-createdAt (para consultas por usuario)
```

---

## 📝 Notas Importantes

- **Límite de caracteres**: 140 caracteres por post (validación en Lambda)
- **JWT**: Validar en cada solicitud; usar `access_token` para acceder a recursos
- **Refresh Token**: Implementar rotación automática en cliente
- **Rate Limiting**: Configurar en API Gateway (p.ej., 100 req/min)
- **Logging**: Habilitar CloudWatch para Lambda y API Gateway
- **Monitoring**: Usar CloudWatch Metrics y X-Ray para tracing

---

## 📞 Contacto

Para preguntas o contribuciones, por favor abre un issue en el repositorio o contacta al equipo de desarrollo.

---

**Última actualización**: Enero 2025  
**Versión**: 1.0.0
