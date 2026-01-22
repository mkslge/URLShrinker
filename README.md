# URLShrinker

A URL shortening service inspired by Bitly, built with Spring Boot.

## Overview

URLShrinker converts long URLs into short, shareable links and handles fast redirection using a caching layer.

## Features

- URL shortening and redirection
- storage with MongoDB
- Redis caching for fast(er) lookups
- RESTful API

## Tech Stack

- Java
- Spring Boot
- MongoDB
- Redis

## Getting Started

### Prerequisites

- Java 17+
- MongoDB
- Redis

### Run Locally

```bash
git clone https://github.com/mkslge/URLShrinker.git
cd URLShrinker
./mvnw spring-boot:run
