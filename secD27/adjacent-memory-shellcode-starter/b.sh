x=''
for i in `seq 999`
do
  x=$x`expr $i % 10`
done
echo $x