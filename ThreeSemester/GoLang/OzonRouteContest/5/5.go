package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"os"
	"strings"
)

type Folder struct {
	Dir     string   `json:"dir"`
	Files   []string `json:"files"`
	Folders []Folder `json:"folders"`
}

var c int

func main() {
	var in *bufio.Reader
	var out *bufio.Writer
	in = bufio.NewReader(os.Stdin)
	out = bufio.NewWriter(os.Stdout)
	defer out.Flush()

	//var a, b int
	//fmt.Fscan(in, &a, &b)
	//fmt.Fprint(out, a + b)
	var t int
	fmt.Fscan(in, &t)
	for ; t > 0; t-- {
		c = 0
		var n int
		fmt.Fscan(in, &n)
		txt := make([]byte, 0)
		in.ReadString('\n')
		for ; n > 0; n-- {
			var str string
			str, _ = in.ReadString('\n')
			txt = append(txt, []byte(str)...)
		}
		var rootFolder Folder
		_ = json.Unmarshal(txt, &rootFolder)
		stat := false
		for _, file := range rootFolder.Files {
			l := strings.Split(file, ".")
			if l[len(l)-1] == "hack" {
				c += len(rootFolder.Files)
				stat = true
				break
			}
		}
		for _, folder := range rootFolder.Folders {
			recSearch(stat, folder)
		}
		fmt.Fprintln(out, c)
	}
}

func recSearch(vir bool, folder Folder) {
	stat := false
	if vir {
		c += len(folder.Files)
		stat = true
	} else {
		for _, file := range folder.Files {
			l := strings.Split(file, ".")
			if l[len(l)-1] == "hack" {
				c += len(folder.Files)
				stat = true
				break
			}
		}
	}
	for _, fol := range folder.Folders {
		recSearch(stat, fol)
	}
}
