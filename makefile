VERSION=2.2.6

all : build push

build :
	DOCKER_BUILDKIT=0 docker build -t usa6463/community-crawler:$(VERSION) .

push :
	docker push usa6463/community-crawler:$(VERSION)