# AWS Lambda Kotlin + CDK

Este proyecto es una implementación de referencia de una función **AWS Lambda** escrita en **Kotlin 21**, utilizando **AWS CDK (Cloud Development Kit)** para la gestión de infraestructura como código (IaC).

## 🚀 Tecnologías

*   **Lenguaje:** [Kotlin 21](https://kotlinlang.org/)
*   **Construcción:** [Gradle](https://gradle.org/)
*   **IaC:** [AWS CDK v2](https://aws.amazon.com/cdk/)
*   **Runtime:** Java 21 (AWS Lambda)
*   **CI/CD:** GitHub Actions

## 📂 Estructura del Proyecto

*   `src/main/kotlin`: Contiene el código fuente de la función Lambda (`Handler.kt`).
*   `infrastructure/`: Contiene la definición de la infraestructura con AWS CDK.
    *   `src/main/kotlin/com/davinchicoder/InfraStack.kt`: Definición del Stack de CloudFormation.
*   `.github/workflows/`: Flujos de trabajo automatizados para despliegue y destrucción.

## 🛠️ Requisitos Previos

*   **Java 21** instalado.
*   **Node.js** (v24+) y **npm** instalados.
*   **AWS CLI** configurado con las credenciales apropiadas.
*   **CDK CLI** instalado globalmente:
    ```bash
    npm install -g aws-cdk
    ```

## ⚙️ Configuración y Uso

### 1. Compilar el Proyecto
Para generar el artefacto de la Lambda (archivo ZIP), ejecuta:
```bash
./gradlew lambdaZip
```
El archivo se generará en `build/libs/lambda.zip`.

### 2. Despliegue Local (CDK)
Desde el directorio raíz:
```bash
# Navegar al directorio de infraestructura
cd infrastructure

# Realizar el bootstrap de CDK (solo la primera vez)
cdk bootstrap

# Desplegar el stack
cdk deploy
```

## 🤖 CI/CD con GitHub Actions

El proyecto incluye dos flujos de trabajo principales:

*   **Deploy CDK (`deploy.yml`)**: Compila el código Kotlin y despliega la infraestructura en AWS. Se activa manualmente mediante `workflow_dispatch`.
*   **Destroy CDK (`destroy.yml`)**: Elimina todos los recursos creados por el stack en AWS.

### Secretos Requeridos en GitHub
Para que los flujos de trabajo funcionen, debes configurar los siguientes secretos en tu repositorio:
*   `AWS_ACCOUNT_ID`: Tu ID de cuenta de AWS.
*   `AWS_REGION`: La región de AWS (ej. `us-east-1`).

## 📝 Detalles de Implementación
*   La Lambda utiliza el runtime `JAVA_21`.
*   Se ha configurado una política de ejecución básica (`AWSLambdaBasicExecutionRole`) para permitir el envío de logs a CloudWatch.
*   El manejador (`handler`) está configurado como `com.davinchicoder.Handler`.
