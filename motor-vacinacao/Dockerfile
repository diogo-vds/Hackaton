FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

RUN mvn dependency:copy-dependencies \
    -DincludeScope=runtime \
    -DoutputDirectory=target/dependency


FROM public.ecr.aws/lambda/java:21

COPY --from=build /app/target/classes ${LAMBDA_TASK_ROOT}
COPY --from=build /app/target/dependency/* ${LAMBDA_TASK_ROOT}/lib/

CMD ["com.fiap.hackathon.mvp.lambda.SchedulerHandler::handleRequest"]