package nyomio

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object ExampleFlinkJob {

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {

        // the port to connect to
        val port: Int
        try {
            val params = ParameterTool.fromArgs(args)
            port = params.getInt("port")
        } catch (e: Exception) {
            System.err.println("No port specified. Please run 'SocketWindowWordCount --port <port>'")
            return
        }

        // get the execution environment
        val env = StreamExecutionEnvironment.getExecutionEnvironment()

        // get input data by connecting to the socket
        val text = env.socketTextStream("192.168.2.100", port, "\n")

        // parse the data, group it, window it, and aggregate the counts
        val windowCounts = text
                .flatMap(FlatMapFunction<String, WordWithCount> { value, out ->
                    for (word in value.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                        out.collect(WordWithCount(word, 1L))
                    }
                })
                .keyBy("word")
                .timeWindow(Time.seconds(5), Time.seconds(1))
                .reduce { a, b -> WordWithCount(a.word, a.count + b.count) }

        // print the results with a single thread, rather than in parallel
        windowCounts.print().setParallelism(1)

        env.execute("Socket Window WordCount")
    }

    // Data type for words with count
    class WordWithCount {

        var word: String = ""
        var count: Long = 0

        constructor() {}

        constructor(word: String, count: Long) {
            this.word = word
            this.count = count
        }

        override fun toString(): String {
            return "$word : $count"
        }
    }
}
