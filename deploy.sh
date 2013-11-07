#!/bin/sh

if [ $# -ne 2 ]; then
	echo 1>&2 Usage: ./build.sh branch release|none
	exit 0
fi

# checkout the proper branch
git checkout $1

# get the git revision number
gitvers=`git describe`

name=""
if [ "$1" == "master" ]; then
	name=$gitvers
else
	name="$gitvers-$1"
fi

nameNoV=`echo $name | cut -c 2-`

echo "Setting Version: $nameNoV"

if [ "$2" == "release" ]; then

	# generate docs
	# javadoc -d docs-$name -sourcepath src/main/java -subpackages me.botsko.prism
else
	nameNoV = "$nameNoV-SNAPSHOT"
fi

# Build maven
mvn deploy -Ddescribe=$nameNoV

echo "DEPLOY COMPLETE"