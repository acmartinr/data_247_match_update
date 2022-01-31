import javax.inject.*;
import play.*;
import play.filters.cors.CORSFilter;
import play.mvc.EssentialFilter;
import play.http.HttpFilters;


@Singleton
public class Filters implements HttpFilters {

    private final Environment env;
    private final CORSFilter corsFilter;

    @Inject
    public Filters(Environment env, CORSFilter corsFilter) {
        this.env = env;
        this.corsFilter = corsFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[] {corsFilter.asJava()};
    }

}
