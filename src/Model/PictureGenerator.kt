package Model

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class PictureGenerator {
    companion object {
        fun getPictureCount() : Int {
            var ret: Int = 0
            File("images/").walk().forEach {
                if(it.toPath().toString().endsWith(".jpg") || it.toPath().toString().endsWith(".jpeg") || it.toPath().toString().endsWith(".png")) {
                    ret++
                }
            }
            return ret
        }

        fun getUslovVrste(n : Int,m : Int, nonogram : Array<Array<Int>>?,i : Int) : Array<Int> {
            var cnt:Int = 0
            var ccnt:Int = 0
            for(j in 0..(m-1)) {
                if(nonogram!![i][j]==1) {
                    cnt++
                }
                else {
                    if(cnt>0) {
                        ccnt++
                        cnt = 0
                    }
                }
            }
            if(cnt>0) {
                ccnt++
            }
            cnt = 0
            var ret:Array<Int> = Array<Int>(ccnt) {-1}

            ccnt = 0
            for(j in 0..(m-1)) {
                if(nonogram!![i][j]==1) {
                    cnt++
                }
                else {
                    if(cnt>0) {
                        ret!![ccnt] = cnt
                        ccnt++
                        cnt = 0
                    }
                }
            }
            if(cnt>0) {
                ret!![ccnt] = cnt
                ccnt++
            }

            return ret

        }

        fun getUslovKolone(n : Int,m : Int, nonogram : Array<Array<Int>>?,j : Int) : Array<Int> {
            var cnt:Int = 0
            var ccnt:Int = 0
            for(i in 0..(n-1)) {
                if(nonogram!![i][j]==1) {
                    cnt++
                }
                else {
                    if(cnt>0) {
                        ccnt++
                        cnt = 0
                    }
                }
            }
            if(cnt>0) {
                ccnt++
            }
            cnt = 0
            var ret:Array<Int> = Array<Int>(ccnt) {-1}

            ccnt = 0
            for(i in 0..(n-1)) {
                if(nonogram!![i][j]==1) {
                    cnt++
                }
                else {
                    if(cnt>0) {
                        ret!![ccnt] = cnt
                        ccnt++
                        cnt = 0
                    }
                }
            }
            if(cnt>0) {
                ret!![ccnt] = cnt
                ccnt++
            }

            return ret

        }

        fun generateNonogramFromGivenPicture(pth: String,sz: Int, fill : Boolean): Nonogram {
            var imageFile : File = File(pth)
            var n: Int = (Random.nextInt(4) + 1) * 5
            var m = n
            if(sz != 0 ) {
                n = sz
                m = sz
            }
            val image : BufferedImage = ImageIO.read(imageFile)
            val w : Int = image.width
            val h : Int = image.height
            var nonogram : Array<Array<Int>>? = Array(n){Array(m) {-1} };
            val pw : Int = w/m
            val ph : Int = h/n
            if(fill) {
                var threshold: Int = 255 * ph * pw * 3 / 2
                for (i in 0..(n - 1)) {
                    for (j in 0..(m - 1)) {
                        var sum: Int = 0
                        for (k in 0..(ph - 1)) {
                            for (l in 0..(pw - 1)) {
                                val c: Color = Color(image.getRGB(j * pw + l, i * ph + k))
                                sum = sum + c.red + c.blue + c.green
                            }
                        }
                        //println(i.toString() + " " + j.toString() + " " + sum.toString() + " " + threshold.toString())
                        if (sum < threshold) {
                            nonogram!![i][j] = 1
                        } else {
                            nonogram!![i][j] = 0
                        }
                    }
                }
                var usloviVrsta: Array<Array<Int>> = Array(n) { i -> getUslovVrste(n, m, nonogram, i) }

                var usloviKolona: Array<Array<Int>> = Array(m) { i -> getUslovKolone(n, m, nonogram, i) }

                var nono: Nonogram = Nonogram(n, m, usloviVrsta, usloviKolona)

                return nono
            }
            else {

                var threshold: Int = 200
                for (i in 0..(n - 1)) {
                    for (j in 0..(m - 1)) {
                        var mdif: Int = 0
                        for (k in 0..(ph - 1)) {
                            for (l in 0..(pw - 1)) {
                                val c: Color = Color(image.getRGB(j * pw + l, i * ph + k))
                                for (x in (-1)..(1)) {
                                    for (y in (-1)..(1)) {
                                        var cx : Int = j * pw + l + x
                                        var cy : Int = i * ph + k + y
                                        if(0<=cx && cx<w && 0<=cy && cy<h) {
                                            val d: Color = Color(image.getRGB(cx,cy))
                                            mdif = max(mdif, abs(c.red-d.red)+ abs(c.blue-d.blue)+abs(c.green-d.green))
                                        }
                                    }
                                }
                            }
                        }
                        //println(i.toString() + " " + j.toString() + " " + sum.toString() + " " + threshold.toString())
                        if (mdif > threshold) {
                            nonogram!![i][j] = 1
                        } else {
                            nonogram!![i][j] = 0
                        }
                    }
                }
                var usloviVrsta: Array<Array<Int>> = Array(n) { i -> getUslovVrste(n, m, nonogram, i) }

                var usloviKolona: Array<Array<Int>> = Array(m) { i -> getUslovKolone(n, m, nonogram, i) }

                var nono: Nonogram = Nonogram(n, m, usloviVrsta, usloviKolona)


                //Za debug:
                /*for(i in 0..(n-1)) {
                    for(j in 0..(m-1)) {
                        nono.setNonogram(i,j,nonogram!![i][j])
                    }
                }*/


                return nono

            }

            /*
            //Za debug:
            for(i in 0..(n-1)) {
                for(j in 0..(m-1)) {
                    nono.setNonogram(i,j,nonogram!![i][j])
                }
            }
            */
        }

        fun generateNonogramFromPicture(ind: Int,n: Int,m: Int, fill : Boolean): Nonogram {
            var imageFile : File = File("images/axe.jpg")

            var cnt: Int = 0
            File("images/").walk().forEach {
                if(it.toPath().toString().endsWith(".jpg") || it.toPath().toString().endsWith(".jpeg") || it.toPath().toString().endsWith(".png")) {
                    if(cnt==ind) {
                        imageFile = it
                    }
                    cnt++
                }
            }


            val image : BufferedImage = ImageIO.read(imageFile)
            val w : Int = image.width
            val h : Int = image.height
            var nonogram : Array<Array<Int>>? = Array(n){Array(m) {-1} };
            val pw : Int = w/m
            val ph : Int = h/n
            if(fill) {
                var threshold: Int = 255 * ph * pw * 3 / 2
                for (i in 0..(n - 1)) {
                    for (j in 0..(m - 1)) {
                        var sum: Int = 0
                        for (k in 0..(ph - 1)) {
                            for (l in 0..(pw - 1)) {
                                val c: Color = Color(image.getRGB(j * pw + l, i * ph + k))
                                sum = sum + c.red + c.blue + c.green
                            }
                        }
                        //println(i.toString() + " " + j.toString() + " " + sum.toString() + " " + threshold.toString())
                        if (sum < threshold) {
                            nonogram!![i][j] = 1
                        } else {
                            nonogram!![i][j] = 0
                        }
                    }
                }
                var usloviVrsta: Array<Array<Int>> = Array(n) { i -> getUslovVrste(n, m, nonogram, i) }

                var usloviKolona: Array<Array<Int>> = Array(m) { i -> getUslovKolone(n, m, nonogram, i) }

                var nono: Nonogram = Nonogram(n, m, usloviVrsta, usloviKolona)

                return nono
            }
            else {

                var threshold: Int = 200
                for (i in 0..(n - 1)) {
                    for (j in 0..(m - 1)) {
                        var mdif: Int = 0
                        for (k in 0..(ph - 1)) {
                            for (l in 0..(pw - 1)) {
                                val c: Color = Color(image.getRGB(j * pw + l, i * ph + k))
                                for (x in (-1)..(1)) {
                                    for (y in (-1)..(1)) {
                                        var cx : Int = j * pw + l + x
                                        var cy : Int = i * ph + k + y
                                        if(0<=cx && cx<w && 0<=cy && cy<h) {
                                            val d: Color = Color(image.getRGB(cx,cy))
                                            mdif = max(mdif, abs(c.red-d.red)+ abs(c.blue-d.blue)+abs(c.green-d.green))
                                        }
                                    }
                                }
                            }
                        }
                        //println(i.toString() + " " + j.toString() + " " + sum.toString() + " " + threshold.toString())
                        if (mdif > threshold) {
                            nonogram!![i][j] = 1
                        } else {
                            nonogram!![i][j] = 0
                        }
                    }
                }
                var usloviVrsta: Array<Array<Int>> = Array(n) { i -> getUslovVrste(n, m, nonogram, i) }

                var usloviKolona: Array<Array<Int>> = Array(m) { i -> getUslovKolone(n, m, nonogram, i) }

                var nono: Nonogram = Nonogram(n, m, usloviVrsta, usloviKolona)


                //Za debug:
                /*for(i in 0..(n-1)) {
                    for(j in 0..(m-1)) {
                        nono.setNonogram(i,j,nonogram!![i][j])
                    }
                }*/


                return nono

            }

            /*
            //Za debug:
            for(i in 0..(n-1)) {
                for(j in 0..(m-1)) {
                    nono.setNonogram(i,j,nonogram!![i][j])
                }
            }
            */
        }
    }
}