package accounting

class PostingService {
    def post(postings) {
        postings.each { posting ->
            posting.save()
            posting.post()
        }
    }
}
