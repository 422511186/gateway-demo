echo "running..."

echo "****************************************"
echo "**          name: xelatex             **"
echo "**     ZhenYu huang    Jiang Tao      **"
echo "**          version: 1.0              **"
echo "**          date: 2022                **"
echo "****************************************"

cd ./%1
xelatex %2.tex

echo "Delete Intermediate files"
@echo off
del ctextemp*
del *.aux
del *.tex.bak
del *.log
del *.nav
del *.out
del *.djs
del *.out.bak
del *.snm
del *.ps
del *.dvi
del *.synctex.gz*
del *.idx
del *.toc
del *.bbl
del *.blg
del *.synctex*

echo "stop..."