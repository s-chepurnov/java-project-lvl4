setup:
	gradle wrapper --gradle-version 8.9

clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	./gradlew run

install:
	./gradlew install

start-dist:
	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

.PHONY: build