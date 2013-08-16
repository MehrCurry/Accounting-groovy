package accounting

class PostingService {
    def post(Posting postings) {
        postings.each { posting ->
            posting.save(failOnError: true)
            posting.doPost()
        }
    }
}
