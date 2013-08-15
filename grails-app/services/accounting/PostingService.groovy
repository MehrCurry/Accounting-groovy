package accounting

class PostingService {
    def post(Posting posting) {
        posting.save()
        posting.post()
    }
}
