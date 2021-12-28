# blv-springboot
Spring Boot backend for the Blaasveld.net project

## Running the project

Run the project with `./mvnw spring-boot:run`

Or
1. Package the project with `./mvnw package`
1. Launch the jar with `java -jar ./target/spring-boot-0.0.1-SNAPSHOT.jar`

More information at [https://spring.io/quickstart](https://spring.io/quickstart)

## Running with Docker

1. Package the project with `./mvnw package`
1. Build the container with `docker build . -t jbrems/blv-springboot`
1. Run the container with `docker run -p 3000:8080 jbrems/blv-springboot`


## Deploy the container to Google Cloud Run

1. Build the container with `docker build . -t jbrems/blv-springboot`
1. Make sure you are logged in to Google Cloud or login with `gcloud auth login`.
1. Make sure `blaasveld-net` is the active project with `gcloud config list` or run `gcloud config set project blaasveld-net`.
1. Run `gcloud builds submit --tag gcr.io/blaasveld-net/blv-springboot` to upload the container image to Google Cloud.
1. Start the container with `gcloud run deploy --image gcr.io/blaasveld-net/blv-springboot`. Choose `y` when asked to allow unauthenticated invocations.