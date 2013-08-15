package accounting

class PostingService {
    static transactional = true

    def post(Posting posting) {
        posting.post()
    }
}
