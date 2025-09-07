import requests
import os
from modules.cmd_runner import CmdRunner
import sys

def load_global_specs():
	daroot: str = None
	artifacts: list = []
	path: str = os.path.expanduser("~/.darrc")
	if not os.path.exists(path):
		open(path, "w").close()
		return None, None
	with open(path, "r") as file:
		file = open(path, "r")
		for line in file:
			line = line.strip()
			if " " not in line:
				continue
			l, r = line.split(" ")
			if l == "root":
				daroot = r
			elif l == "use":
				artifacts.append(r)

	return artifacts, daroot

def update_darrc(daroot: str, artifacts: list):
	path: str = os.path.expanduser("~/.darrc")
	with open(path, "w") as file:
		file.write(f"root {daroot}\n\n")
		written = set()
		for artifact in artifacts:
			if artifact not in written:
				file.write(f"use {artifact}\n")
			written.add(artifact)

def main():
	artifacts, daroot = load_global_specs();
	cmd_runner = CmdRunner(daroot, artifacts, sys.argv)
	cmd_runner.run()
	update_darrc(daroot, artifacts)

if __name__ == "__main__":
	main()
